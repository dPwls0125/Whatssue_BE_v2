package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.dto.CommentCreateDto;
import GDG.whatssue.domain.comment.dto.CommentModifyDto;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PostRepository postRepository;

    public void createComment(Long memberId, CommentCreateDto dto){
        Comment comment = Comment.builder()
                .parentComment(Optional.ofNullable(dto.getParentId()).flatMap(commentRepository::findById).orElse(null))
                .post(postRepository.findById(dto.getPostId()).get())
                .clubMember(clubMemberRepository.findById(memberId).get())
                .content(dto.getContent())
                .hidden(false)
                .build();
        commentRepository.save(comment);
    }

    public void updateComment(Long memberId, CommentModifyDto dto) {
        checkCommentWriterId(memberId,dto.getWriter_memberId());
        Comment comment = commentRepository.findById(dto.getCommentId()).get();
        comment.setContent(dto.getContent());
        commentRepository.save(comment);
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

    private void checkCommentWriterId(Long postMemberId, Long memberId) {
        if(!postMemberId.equals(memberId)){
            throw new IllegalArgumentException("작성자만 수정 및 삭제채 가능합니다.");
        }
    }
}


