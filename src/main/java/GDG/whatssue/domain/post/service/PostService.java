package GDG.whatssue.domain.post.service;

import static GDG.whatssue.domain.file.FileConst.POST_IMAGE_DIRNAME;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.post.dto.UpdatePostRequest;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.entity.PostCategory;
import GDG.whatssue.domain.like.entity.PostLike;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.post.repository.PostQueryRepository;
import GDG.whatssue.domain.post.repository.PostRepository;
import GDG.whatssue.global.error.CommonException;
import java.io.IOException;
import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import GDG.whatssue.global.util.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final FileRepository fileRepository;
    private final PostQueryRepository postQueryRepository;

    @Transactional
    public void addPost(Long clubId, Long userId, AddPostRequest request, List<MultipartFile> postImages)
        throws IOException {
        Club club = clubRepository.findById(clubId).get();
        ClubMember writer = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId).get();

        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버

        if (request.getPostCategory() == PostCategory.NOTICE && clubMember.getRole() != Role.MANAGER) {
            throw new CommonException(PostErrorCode.EX7200);//공지 게시글 작성 권한이 없습니다.
        }
        //게시글 db 저장
        Post post = request.toEntity(club, writer);
        postRepository.save(post);

        //이미지 s3 업로드, db 저장
        uploadPostImages(postImages, post);
    }

    public GetPostResponse getPost(Long postId) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글

        //게시글 이미지 Path List
        List <String> postImages = new ArrayList<>();
        List <UploadFile> storeFileNames = post.getPostImageFiles();
        if (storeFileNames != null) {
            for (UploadFile storeFileName : storeFileNames){
                postImages.add(S3Utils.getFullPath(storeFileName.getStoreFileName()));
            }
        }

        //작성자 프로필 이미지 Path
        String memberProfileImage = S3Utils.getFullPath(post.getWriter().getProfileImage().getStoreFileName());

        // 좋아요 목록 가져오기
        Long postLikeCount = Long.valueOf(post.getPostLikeList().size());

        return GetPostResponse.builder()
            .postId(post.getId())
            .writerName(post.getWriter().getMemberName())
            .postCategory(post.getPostCategory())
            .postTitle(post.getPostTitle())
            .postContent(post.getPostContent())
            .writerProfileImage(memberProfileImage)
            .uploadImage(postImages)
            .postLikeCount(postLikeCount).build();
    }

    @Transactional
    public void uploadPostImages(List<MultipartFile> postImages, Post post) throws IOException {
        if (postImages != null) {
            for (MultipartFile postImage : postImages) {
                UploadFile imageFile = fileUploadService.uploadFile(postImage, POST_IMAGE_DIRNAME);
                post.addPostImageFile(imageFile);
                fileRepository.save(imageFile);
            }
        }
    }

    @Transactional
    public void deletePostImages(Post post) throws IOException {
        List <String> deleteImagesNames = new ArrayList<>();
        List<UploadFile> deleteImages = post.getPostImageFiles();
        if(deleteImages != null){
            for(UploadFile deleteImage : deleteImages){
                fileUploadService.deleteFile(deleteImage.getStoreFileName());
            }
            post.clearPostImageFiles();
        }
    }

    @Transactional
    public void deletePost(Long memberId, Long postId) throws IOException {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글

        if (post.getPostCategory() == PostCategory.NOTICE && !clubMember.checkManagerRole()) {
                throw new CommonException(PostErrorCode.EX7202);//공지 삭제 권한 X
            }
        if (post.getWriter().getId() != clubMember.getId() && !clubMember.checkManagerRole()) {
            throw new CommonException(PostErrorCode.EX7204);//작성자만 삭제 가능
        }
            //이미지 삭제 = CasCadeType.REMOVE

            postRepository.delete(post);
    }

    public void updatePost(Long clubId, Long memberId, Long postId, UpdatePostRequest request, List<MultipartFile> postImages) throws IOException {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글
        if (post.getPostCategory() == PostCategory.NOTICE && !clubMember.checkManagerRole()) {
            throw new CommonException(PostErrorCode.EX7201);//공지 수정 권한 X
        }
        if (post.getWriter().getId() != clubMember.getId() && !clubMember.checkManagerRole()) {
            throw new CommonException(PostErrorCode.EX7203);//작성자만 수정 가능
        }

        post.updatePost(request.getPostTitle(), request.getPostContent(), request.getPostCategory(), clubMember);

        //기존 이미지 삭제 TODO
        deletePostImages(post);

        //새로운 이미지 s3 업로드, db 저장
        uploadPostImages(postImages, post);
        postRepository.save(post);
    }
    public Page<GetPostResponse> getPostList(Long clubId, String keyword, LocalDateTime startDate, LocalDateTime endDate, String sortBy, Pageable pageable) {
        Page<Post> posts = postQueryRepository.findPosts(clubId, keyword, startDate, endDate, sortBy, pageable);
        return posts.map(post -> GetPostResponse.builder()
                .postId(post.getId())
                .writerName(post.getWriter().getMemberName())
                .postCategory(post.getPostCategory())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .writerProfileImage(post.getWriter().getProfileImage() != null ? post.getWriter().getProfileImage().getStoreFileName() : null)
                .uploadImage(post.getPostImageFiles().stream().map(file -> file.getStoreFileName()).collect(Collectors.toList()))
                .postLikeCount((long)post.getPostLikeList().size())
                .build());
    }
}
