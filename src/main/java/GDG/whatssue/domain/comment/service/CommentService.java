package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment() {
        // 댓글 작성
    }

    public void updateComment() {
        // 댓글 수정
    }

    public void deleteComment() {
        // 댓글 삭제
    }

    public void getComment() {
        // 댓글 조회
    }

    public void getCommentList() {
        // 댓글 목록 조회
    }


}
