package GDG.whatssue.domain.post.controller;

import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.global.common.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "PostController", description = "모임의 게시글에 관련된 api")
@RestController
@RequestMapping("/api/clubs/{clubId}/posts")
public class PostController {

    @Operation
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity addPost(
        @PathVariable(name = "clubId") Long clubId,
        @LoginMember Long memberId,
        @RequestPart("request") AddPostRequest request,
        @RequestPart("postImages") List<MultipartFile> postImages) {
        
        //addPost 처리 TODO
        return null;
    }
}
