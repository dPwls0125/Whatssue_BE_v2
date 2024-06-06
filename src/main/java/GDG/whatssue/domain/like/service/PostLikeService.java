package GDG.whatssue.domain.like.service;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.like.entity.PostLike;
import GDG.whatssue.domain.like.exeption.PostLikeErrorCode;
import GDG.whatssue.domain.like.repository.PostLikeRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.officialabsence.exception.OfficialAbsenceErrorCode;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.domain.post.repository.PostRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PostRepository postRepository;
    private final ClubRepository clubRepository;
    @Transactional
    public void createPostLike(Long clubId, Long memberId, Long postId) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100)); // 존재하지 않는 게시글

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CommonException(ClubErrorCode.EX3100)); // 존재하지 않는 모임

        if (postLikeRepository.findByPostAndClubMemberAndPost_Club_Id(post, clubMember, clubId).isPresent()) {
            throw new CommonException(PostLikeErrorCode.EX10200); // 이미 좋아요를 누른 게시물
        }

        PostLike postLike = new PostLike(post, clubMember, club);
        postLikeRepository.save(postLike);
    }
    @Transactional
    public void deletePostLike(Long clubId, Long memberId, Long postId) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(PostErrorCode.EX7100)); // 존재하지 않는 게시글

        PostLike postLike = postLikeRepository.findByPostAndClubMemberAndPost_Club_Id(post, clubMember, clubId)
                .orElseThrow(() -> new CommonException(PostLikeErrorCode.EX10201)); // 좋아요를 누르지 않은 게시물

        postLikeRepository.delete(postLike);
    }
}

