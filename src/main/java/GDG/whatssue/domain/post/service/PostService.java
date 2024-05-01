package GDG.whatssue.domain.post.service;

import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void addPost(Long clubId, Long memberId, AddPostRequest request, List<MultipartFile> postImages) {
        //clubId, memberId TODO
        postRepository.save(request.toEntity());
    }

}
