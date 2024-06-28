package GDG.whatssue.domain.comment.service;


import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentDto;
import GDG.whatssue.domain.comment.dto.CommentUpdateDto;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.exception.CommentErrorCode;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.service.PostService;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final ClubMemberService clubMemberService;
    private final PostService postService;

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
    public Page<CommentDto> getCommentList(Long postId, int size, int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        Page<Comment> commentsPage = commentRepository.findAllByPostIdAndParentCommentIsNullAndDeleteAtIsNull(postId, pageable);

        // 모든 댓글을 CommentDto로 변환하여 반환
        return commentsPage.map(CommentDto::of);
    }

    public Page<CommentDto> getChildCommentList(Long parentId, int size, int page){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        Page<Comment> comments = commentRepository.findByParentComment_Id(parentId, pageable);
        return comments.map(CommentDto::of);
    }

    @Override
    public void getMyCommentList(Long userId, Long clubId) {

    }

    private Long getClubMemberId(Long clubId, Long userId) {
        return clubMemberService.getClubMemberId(clubId, userId);
    }

    private ClubMember getClubMember(Long clubId, Long userId) {
        return clubMemberService.getClubMember(clubId, userId);
    }
    private Post getPost(Long postId) {
        return postService.getPost(postId);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    private void checkAuthorization(Comment comment, Long clubId, Long userId) {

        if(comment.getClubMember() != getClubMember(clubId, userId) || getClubMember(clubId,userId) == null)
            throw new CommonException(CommentErrorCode.EX8101);

    }

}
