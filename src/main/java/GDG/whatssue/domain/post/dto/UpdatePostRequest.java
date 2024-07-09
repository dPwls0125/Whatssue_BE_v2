package GDG.whatssue.domain.post.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.entity.PostCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class UpdatePostRequest {
    private String postTitle;
    private String postContent;
    private List<String> deleteImages;
    private Map<Integer, String> maintainImages;
}