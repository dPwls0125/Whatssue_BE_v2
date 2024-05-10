package GDG.whatssue.domain.post.service;

import static GDG.whatssue.global.common.FileConst.POST_IMAGE_DIRNAME;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.domain.post.repository.PostRepository;
import GDG.whatssue.global.error.CommonException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void addPost(Long clubId, Long memberId, AddPostRequest request, List<MultipartFile> postImages)
        throws IOException {
        Club club = clubRepository.findById(clubId).get();
        ClubMember writer = clubMemberRepository.findById(memberId).get();

        //게시글 db 저장
        Post savedPost = postRepository.save(request.toEntity(club, writer));

        //이미지 s3 업로드, db 저장
        uploadPostImages(postImages,  club, savedPost);
    }

    public GetPostResponse getPost(Long postId) {
        //사진 이미지 반환 TODO

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CommonException(PostErrorCode.POST_NOT_FOUND_ERROR));

        return GetPostResponse.builder()
            .postId(post.getId())
            .writerName(post.getWriter().getMemberName())
            .postCategory(post.getPostCategory())
            .postTitle(post.getPostTitle())
            .postContent(post.getPostContent()).build();
//            .writerProfileImage()
//            .uploadImage()

    }

    @Transactional
    public void uploadPostImages(List<MultipartFile> postImages, Club club, Post savedPost) throws IOException {
        if (postImages == null) {
            return;
        }

        for (MultipartFile postImage : postImages) {
            //s3에 저장 처리
            String storeFileName = fileUploadService.saveFile(postImage, POST_IMAGE_DIRNAME);

            //fileDB에 저장
            String originalFileName = postImage.getOriginalFilename();

            fileRepository.save(UploadFile.builder()
                .club(club)
                .post(savedPost)
                .uploadFileName(originalFileName)
                .storeFileName(storeFileName).build());
        }
    }

}
