package GDG.whatssue.domain.comment.service;


import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void deleteComment(Long commentId, Long userId, Long clubId) {



    }

    @Override
    public void updateComment(Long commentId, String content) {

    }

    @Override
    public List<Comment> getCommentList(Long postId) {
        return null;
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

}
