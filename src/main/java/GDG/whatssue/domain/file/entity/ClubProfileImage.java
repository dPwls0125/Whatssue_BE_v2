package GDG.whatssue.domain.file.entity;

import GDG.whatssue.domain.club.entity.Club;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubProfileImage extends UploadFile{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    public void setClub(Club club) {
        this.club = club;
    }

    //==생성 메서드==//
    private ClubProfileImage(String uploadFileName, String storeFileName) {
        super(uploadFileName, storeFileName);
    }

    public static ClubProfileImage of(String uploadFileName, String storeFileName) {
        return new ClubProfileImage(uploadFileName, storeFileName);
    }
}
