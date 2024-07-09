package GDG.whatssue.domain.post.service;

import static GDG.whatssue.domain.file.FileConst.POST_IMAGE_DIRNAME;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.comment.service.CommentService;
import GDG.whatssue.domain.file.entity.PostImage;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.post.repository.PostLikeRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.post.dto.UpdatePostRequest;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.entity.PostCategory;
import GDG.whatssue.domain.post.entity.PostLike;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.post.repository.PostQueryRepository;
import GDG.whatssue.domain.post.repository.PostRepository;
import GDG.whatssue.global.error.CommonException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import GDG.whatssue.global.util.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private static final int MAX_IMAGE_LIST_SIZE = 10;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final FileRepository fileRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

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

        if(postImages.size()>10){
            throw new CommonException(PostErrorCode.EX7207);//사진은 최대 10장 업로드 가능
        }

        //게시글 db 저장
        Post post = request.toEntity(club, writer);
        postRepository.save(post);

        //이미지 s3 업로드, db 저장
        uploadPostImages(postImages, post);
    }

    public GetPostResponse getPost(Long clubId, Long userId, Long postId) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글

        //게시글 이미지 Path List
        Map<Integer, String> postImages = new HashMap<>();
        List <PostImage> storeFileNames = post.getPostImageFiles();
        if (storeFileNames != null) {
            for (PostImage storeFileName : storeFileNames){
                postImages.put(storeFileName.getOrderNum(),S3Utils.getFullPath(storeFileName.getStoreFileName()));
            }
        }

        //작성자 프로필 이미지 Path
        String memberProfileImage = S3Utils.getFullPath(post.getWriter().getProfileImage().getStoreFileName());

        // 좋아요 수 가져오기
        Long postLikeCount = Long.valueOf(post.getPostLikeList().size());

        // 댓글 수 가져오기
        Long commentCount = commentCount(postId);


        // 좋아요 여부 체크
        Boolean postLikeCheck = isLikedCheck(userId, postId, clubId);

        return GetPostResponse.builder()
                .postId(post.getId())
                .writerName(post.getWriter().getMemberName())
                .postCategory(post.getPostCategory())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .writerProfileImage(memberProfileImage)
                .uploadImage(postImages)
                .postLikeCount(postLikeCount)
                .commentCount(commentCount)
                .isLiked(postLikeCheck)
                .createdAt(post.getCreateAt())
                .build();
    }
    public Long commentCount(Long postId){
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createAt").ascending());

        Page<Comment> commentsPage = commentRepository.findFilteredParentComments(postId, pageable);
        Long commentCount = commentsPage.getTotalElements();
        for(Comment comment : commentsPage){
            commentCount+=commentRepository.findByParentComment_IdAndDeleteAtIsNull(comment.getId(),pageable).getTotalElements();
        }
        return commentCount;
    }

    public Boolean isLikedCheck(Long userId, Long postId, Long clubId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글

        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버

        Boolean postLikeCheck = postLikeRepository.findByPostAndClubMember(post, clubMember).isPresent();

        if (postLikeCheck){
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public void uploadPostImages(List<MultipartFile> files, Post post) throws IOException {
        if (files != null) {
            int orderNum = 1;
            for (MultipartFile file : files) {
                String storeFileName = fileUploadService.uploadFile(file, POST_IMAGE_DIRNAME);
                String originalFileName = fileUploadService.getOriginalFileName(file);
                PostImage postImage = PostImage.of(originalFileName,storeFileName,orderNum);
                post.addPostImageFile(postImage);
                fileRepository.save(postImage);
                orderNum++;
            }
        }
    }

    @Transactional
    public void deleteAllPostImages(Post post) throws IOException {
        List<PostImage> deleteImages = post.getPostImageFiles();
        if(deleteImages != null){
            for(UploadFile deleteImage : deleteImages){
                // S3에서 파일 삭제
                fileUploadService.deleteFile(deleteImage.getStoreFileName());
                // DB에서 파일 삭제
                fileRepository.delete(deleteImage);
            }
            post.clearPostImageFiles();
        }
    }

    @Transactional
    public void deletePost(Long clubId, Long userId, Long postId) throws IOException {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글

        // 공지사항인 경우, 관리자만 삭제 가능
        if (post.getPostCategory() == PostCategory.NOTICE) {
            if (!clubMember.checkManagerRole()) {
                throw new CommonException(PostErrorCode.EX7202); // 공지 삭제 권한 없음
            }
        } else {
            // 공지가 아닌 경우, 작성자 본인이나 관리자만 삭제 가능
            if (!post.getWriter().getId().equals(clubMember.getId()) && !clubMember.checkManagerRole()) {
                throw new CommonException(PostErrorCode.EX7204); // 작성자 본인이나 매니저가 아닌 경우 삭제 권한 없음
            }
        }
        //이미지 삭제
        deleteAllPostImages(post);
        //post 삭제
        postRepository.delete(post);
    }
    @Transactional
    public void updatePost(Long clubId, Long userId, Long postId, UpdatePostRequest postRequest, List<MultipartFile> newImages) throws IOException {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글
        validateMemberAbleToUpdatePost(clubMember, post);

        post.updatePost(postRequest.getPostTitle(), postRequest.getPostContent());

        List<String> deleteImages = postRequest.getDeleteImages();
        Map<Integer, String> maintainImages = postRequest.getMaintainImages();


        //삭제 이미지 리스트 삭제 진행
        deletePostImages(deleteImages, post);

        // 유지할 이미지들의 orderNum 업데이트
        if (maintainImages != null) {
            List<PostImage> storeFiles = post.getPostImageFiles();
            for (Map.Entry<Integer, String> entry : maintainImages.entrySet()) {
                Integer orderNum = entry.getKey();
                String fullPath = entry.getValue();
                for (PostImage storeFile : storeFiles) {
                    if (fullPath.equals(S3Utils.getFullPath(storeFile.getStoreFileName()))) {
                        storeFile.changeOrder(orderNum);
                        fileRepository.save(storeFile); // 변경사항 저장
                    }
                }
            }
        }

        //새로운 이미지 업로드
        int checkNewImagesNum = 0;
        for(MultipartFile newImage:newImages){
            if(newImage.isEmpty()) checkNewImagesNum=1;
        }
        if(checkNewImagesNum==0) { // 새로운 이미지가 있는 경우에만
            int nullCount = 0;
            for (int orderNum = 1; orderNum < MAX_IMAGE_LIST_SIZE + 1; orderNum++) { // 최대 이미지 10장
                if (maintainImages.get(orderNum) == null) {
                    System.out.println(nullCount);
                    nullCount++;
                }
            }
            if (newImages.size() <= nullCount) { // 최대 이미지 갯수 조건 비교
                int newImagesFlag = 0;
                for (int orderNum = 1; orderNum < MAX_IMAGE_LIST_SIZE + 1; orderNum++) {
                    String s = maintainImages.get(orderNum); // get(key) / key == orderNum
                    if (s == null) { // orderNum이 비어있는 경우
                        if (newImagesFlag < newImages.size()) {
                            uploadPostImage(newImages.get(newImagesFlag), orderNum, post); // newImages가 차례로 빈 자리로 할당.
                            newImagesFlag++;
                        }
                    }
                }
            } else {
                throw new CommonException(PostErrorCode.EX7207); //사진은 10장 까지만 업로드 가능
            }
        }
    }

    private void validateMemberAbleToUpdatePost(ClubMember clubMember, Post post) {
        // 공지사항인 경우, 관리자만 수정 가능
        if (post.getPostCategory() == PostCategory.NOTICE) {
            if (!clubMember.checkManagerRole()) {
                throw new CommonException(PostErrorCode.EX7201); // 공지 수정 권한 없음
            }
        } else {
            // 공지가 아닌 경우, 작성자 본인이나 관리자만 수정 가능
            if (!post.getWriter().getId().equals(clubMember.getId()) && !clubMember.checkManagerRole()) {
                throw new CommonException(PostErrorCode.EX7203); // 작성자 본인이나 매니저가 아닌 경우 수정 권한 없음
            }
        }
    }
    @Transactional
    public void uploadPostImage(MultipartFile file, int orderNum, Post post) throws IOException {
        if (file != null) {
            String storeFileName = fileUploadService.uploadFile(file, POST_IMAGE_DIRNAME);
            String originalFileName = fileUploadService.getOriginalFileName(file);
            PostImage postImage = PostImage.of(originalFileName,storeFileName, orderNum);
            post.addPostImageFile(postImage);
            fileRepository.save(postImage);
        }
    }
    @Transactional
    public void deletePostImages(List<String> deleteImagesPath, Post post) throws IOException {
        List<PostImage> storeFiles = post.getPostImageFiles();
        List<PostImage> filesToRemove = new ArrayList<>();

        if(deleteImagesPath != null){
            for(String deleteImagePath : deleteImagesPath) {
                for (PostImage storeFile : storeFiles) {
                    if (deleteImagePath.equals(S3Utils.getFullPath(storeFile.getStoreFileName()))){
                        // S3에서 파일 삭제
                        fileUploadService.deleteFile(storeFile.getStoreFileName());
                        // DB에서 파일 삭제
                        fileRepository.delete(storeFile);
                        // 제거할 파일을 목록에 추가
                        filesToRemove.add(storeFile);
                    }
                }
            }
            // Post에서 파일 제거
            for (PostImage fileToRemove : filesToRemove) {
                post.removePostImageFile(fileToRemove);
            }
        }
    }
    public Page<GetPostResponse> getPostList(Long clubId, Long userId, String keyword, LocalDateTime startDate, LocalDateTime endDate, String sortBy, PostCategory category, Pageable pageable) {
        Page<Post> posts = postQueryRepository.findPosts(clubId, keyword, startDate, endDate, sortBy, category, pageable);
        List<GetPostResponse> getPostResponses = new ArrayList<>();

        for (Post post : posts) {
            //게시글 이미지 Path List
            Map<Integer, String> postImages = new HashMap<>();
            List <PostImage> storeFileNames = post.getPostImageFiles();
            if (storeFileNames != null) {
                for (PostImage storeFileName : storeFileNames){
                    postImages.put(storeFileName.getOrderNum(),S3Utils.getFullPath(storeFileName.getStoreFileName()));
                }
            }
            // 작성자 프로필 이미지 Path
            String memberProfileImage = post.getWriter().getProfileImage() != null ? S3Utils.getFullPath(post.getWriter().getProfileImage().getStoreFileName()) : null;

            // 좋아요 수
            Long postLikeCount = (long) post.getPostLikeList().size();

            // 댓글 수 가져오기
            Long commentCount = commentCount(post.getId());

            // 좋아요 여부 체크
            Boolean postLikeCheck = isLikedCheck(userId, post.getId(), clubId);

            GetPostResponse response = GetPostResponse.builder()
                    .postId(post.getId())
                    .writerName(post.getWriter().getMemberName())
                    .postCategory(post.getPostCategory())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .writerProfileImage(memberProfileImage)
                    .uploadImage(postImages)
                    .postLikeCount(postLikeCount)
                    .commentCount(commentCount)
                    .isLiked(postLikeCheck)
                    .createdAt(post.getCreateAt())
                    .build();

            getPostResponses.add(response);
        }
        return new PageImpl<>(getPostResponses, pageable, posts.getTotalElements());
    }
    @Transactional
    public void createPostLike(Long clubId, Long userId, Long postId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100)); // 존재하지 않는 게시글

        if (postLikeRepository.findByPostAndClubMember(post, clubMember).isPresent()) {
            throw new CommonException(PostErrorCode.EX7205); // 이미 좋아요를 누른 게시물
        }

        PostLike postLike = new PostLike(post, clubMember);
        postLikeRepository.save(postLike);
    }
    @Transactional
    public void deletePostLike(Long clubId, Long userId, Long postId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100)); // 존재하지 않는 게시글

        PostLike postLike = postLikeRepository.findByPostAndClubMember(post, clubMember)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7205)); // 좋아요를 누르지 않은 게시물

        postLikeRepository.delete(postLike);
    }

    public Page<GetPostResponse> getMyPosts(Long clubId, Long userId, Pageable pageable) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        Club club = clubMember.getClub();

        Page<Post> posts = postRepository.findByClubAndWriter(club, clubMember, pageable);

        List<GetPostResponse> getPostResponses = new ArrayList<>();

        for (Post post : posts) {
            //게시글 이미지 Path List
            Map<Integer, String> postImages = new HashMap<>();
            List <PostImage> storeFileNames = post.getPostImageFiles();
            if (storeFileNames != null) {
                for (PostImage storeFileName : storeFileNames){
                    postImages.put(storeFileName.getOrderNum(),S3Utils.getFullPath(storeFileName.getStoreFileName()));
                }
            }
            // 작성자 프로필 이미지 Path
            String memberProfileImage = post.getWriter().getProfileImage() != null ? S3Utils.getFullPath(post.getWriter().getProfileImage().getStoreFileName()) : null;

            // 좋아요 수
            Long postLikeCount = (long) post.getPostLikeList().size();

            // 댓글 수 가져오기
            Long commentCount = commentCount(post.getId());

            // 좋아요 여부 체크
            Boolean postLikeCheck = isLikedCheck(userId, post.getId(), clubId);

            GetPostResponse response = GetPostResponse.builder()
                    .postId(post.getId())
                    .writerName(post.getWriter().getMemberName())
                    .postCategory(post.getPostCategory())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .writerProfileImage(memberProfileImage)
                    .uploadImage(postImages)
                    .postLikeCount(postLikeCount)
                    .commentCount(commentCount)
                    .isLiked(postLikeCheck)
                    .createdAt(post.getCreateAt())
                    .build();

            getPostResponses.add(response);
        }
        return new PageImpl<>(getPostResponses, pageable, posts.getTotalElements());
    }
    public Page<GetPostResponse> getLikedPosts(Long clubId, Long userId, Pageable pageable) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버
        Club club = clubMember.getClub();

        Page<Post> posts = postRepository.findByPostLikeList_ClubMemberAndClub(clubMember, club, pageable);

        List<GetPostResponse> getPostResponses = new ArrayList<>();

        for (Post post : posts) {
            //게시글 이미지 Path List
            Map<Integer, String> postImages = new HashMap<>();
            List <PostImage> storeFileNames = post.getPostImageFiles();
            if (storeFileNames != null) {
                for (PostImage storeFileName : storeFileNames){
                    postImages.put(storeFileName.getOrderNum(),S3Utils.getFullPath(storeFileName.getStoreFileName()));
                }
            }
            // 작성자 프로필 이미지 Path
            String memberProfileImage = post.getWriter().getProfileImage() != null ? S3Utils.getFullPath(post.getWriter().getProfileImage().getStoreFileName()) : null;

            // 좋아요 수
            Long postLikeCount = (long) post.getPostLikeList().size();

            // 댓글 수 가져오기
            Long commentCount = commentCount(post.getId());

            // 좋아요 여부 체크
            Boolean postLikeCheck = isLikedCheck(userId, post.getId(), clubId);

            GetPostResponse response = GetPostResponse.builder()
                    .postId(post.getId())
                    .writerName(post.getWriter().getMemberName())
                    .postCategory(post.getPostCategory())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .writerProfileImage(memberProfileImage)
                    .uploadImage(postImages)
                    .postLikeCount(postLikeCount)
                    .commentCount(commentCount)
                    .isLiked(postLikeCheck)
                    .createdAt(post.getCreateAt())
                    .build();

            getPostResponses.add(response);
        }
        return new PageImpl<>(getPostResponses, pageable, posts.getTotalElements());
    }

    public Post getPost(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글
    }
}
