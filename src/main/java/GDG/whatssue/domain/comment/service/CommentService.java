package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.dto.CommentCreateDto;
import GDG.whatssue.domain.comment.dto.CommentModifyDto;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.comment.repository.CommentRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.repository.PostRepository;
import GDG.whatssue.global.error.CommonErrorCode;
import GDG.whatssue.global.error.CommonException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deleteComment(Long memberId, Long commentId) { // 댓글 숨김

        Comment comment = commentRepository.findById(commentId).get();
        ClubMember writer = comment.getClubMember();
        ClubMember requester = clubMemberRepository.findById(memberId).get();

        // 작성자이거나 매니저일 경우에만 삭제 가능
        if((writer.getId() == memberId) ||  requester.getRole() == Role.MANAGER){
            comment.setHidden(true);
            commentRepository.save(comment);
        }else {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
    }

    @Transactional
    public Comment getComment(Long commentId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("댓글이 존재하지 않습니다."));
        if(comment.isHidden()){
            throw new CommonException(CommonErrorCode.BAD_REQUEST);
        }
        return comment;
    }

    // 댓글 리스트 조회
    // hidden인 것들 제외하고 조회
    @Transactional
    public List getCommentList(Long postId) {
        List<Comment>comments = commentRepository.findByPostId(postId).orElseThrow(() -> new NullPointerException("댓글이 존재하지 않습니다."));
        for(Comment comment : comments){ // 숨김 처리된 댓글 제외
            if(comment.isHidden()){
                comments.remove(comment);
            }
        }
        return comments;
    }

    private void checkCommentWriterId(Long postMemberId, Long memberId) {
        if(!postMemberId.equals(memberId)){
            throw new IllegalArgumentException("작성자만 수정 및 삭제채 가능합니다.");
        }
    }
}


