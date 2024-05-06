package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.dto.CommentRequestDto;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PostRepository postRepository;
    public void createComment(Long memberId, CommentRequestDto dto) {
        Comment comment;
        if(dto.getParentId() == null) {
            comment = Comment.builder()
                    .clubMember(clubMemberRepository.findById(memberId).get())
                    .post(postRepository.findById(dto.getPostId()).get())
                    .content(dto.getContent())
                    .build();
        }else{
            comment = Comment.builder()
                    .parentComment(commentRepository.findById(dto.getParentId()).get())
                    .content(dto.getContent())
                    .build();
        }
        commentRepository.save(comment);
    }

    public void updateComment(Long memberId, CommentRequestDto dto) {
        // 댓글 수정
    }

    public void deleteComment() {
        // 댓글 숨김
    }

    public void getComment() {
        // 댓글 조회
    }

    public void getCommentList() {
        // 댓글 목록 조회
    }

}
