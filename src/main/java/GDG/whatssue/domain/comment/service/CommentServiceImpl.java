package GDG.whatssue.domain.comment.service;


import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentDto;
import GDG.whatssue.domain.comment.dto.CommentUpdateDto;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.exception.CommentErrorCode;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.domain.post.repository.PostRepository;
import GDG.whatssue.domain.post.service.PostService;
import GDG.whatssue.global.error.CommonException;
import GDG.whatssue.global.util.S3Utils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ClubMemberService clubMemberService;

    private final FileUploadService fileUploadService;

    @Override
    public void createComment(CommentAddDto dto, Long userId, Long clubId) {

        Comment comment = Comment.builder()
                .post(getPost(dto.getPostId()))
                .content(dto.getContent())
                .clubMember(getClubMember(clubId, userId))
                .build();

        commentRepository.save(comment);
    }


    @Override
    public void createChildComment(ChildCommentAddDto dto, Long userId, Long clubId) {

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .clubMember(getClubMember(clubId, userId))
                .parentComment(getComment(dto.getParentId()))
                .post(getPost(dto.getPostId()))
                .build();

        commentRepository.save(comment);

    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId, Long clubId) {

        Comment deleteComment = getComment(commentId);

        checkAuthorization(deleteComment, clubId, userId);

        deleteComment.setDeleteAt(LocalDateTime.now());

    }

    @Override
    @Transactional
    public void updateComment(CommentUpdateDto dto, Long userId, Long clubId) {

        Comment modifyComment = getComment(dto.getCommentId());

        checkAuthorization(modifyComment, clubId, userId);

        modifyComment.setContent(dto.getContent());
    }

    @Override
    public Page<CommentDto> getParentCommentList(Long postId, int size, int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").ascending());
        Page<Comment> commentsPage = commentRepository.findFilteredParentComments(postId, pageable);

        List<CommentDto> commentDtos = commentsPage.stream()
                .map(comment -> CommentDto.of(comment))
                .map(this::nullifyDeletedCommentFields)
                .collect(Collectors.toList());

        return new PageImpl<>(commentDtos, pageable, commentsPage.getTotalElements());
    }

    public Page<CommentDto> getChildCommentList(Long parentId, int size, int page){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").ascending());
        Page<Comment> commentsPage = commentRepository.findByParentComment_IdAndDeleteAtIsNull(parentId, pageable);

        return commentsPage.map(comment -> {
            CommentDto commentDto = CommentDto.of(comment);
            return commentDto;
        });
    }

    @Override
    public void getMyCommentList(Long userId, Long clubId) {

    }

    private Long getClubMemberId(Long clubId, Long userId) {
        return clubMemberService.getClubMemberId(clubId, userId);
    }

    private ClubMember getClubMember(Long clubId, Long userId) {
        return clubMemberService.findClubMemberByClubAndUser(clubId, userId).get();
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100));//존재하지 않는 게시글
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    private void checkAuthorization(Comment comment, Long clubId, Long userId) {

        if(comment.getClubMember() != getClubMember(clubId, userId) || getClubMember(clubId,userId) == null)
            throw new CommonException(CommentErrorCode.EX8101);

    }
    public long getChildCommentCount(Long parentId) {
        return commentRepository.countByParentCommentId(parentId);
    }
    private CommentDto nullifyDeletedCommentFields(CommentDto commentDto){
        if(commentDto.getDeleteAt() != null){
            commentDto.setWriterId(null);
            commentDto.setWriterName(null);
            commentDto.setProfileImage(null);
            commentDto.setContent(null);
            commentDto.setCreatedAt(null);
            commentDto.setUpdateAt(null);
        }
        return commentDto;
    }

    private String getProfileImage(Comment comment) {
        String storeFileName = comment.getClubMember().getProfileImage().getStoreFileName();
        return S3Utils.getFullPath(storeFileName);
    }
}

