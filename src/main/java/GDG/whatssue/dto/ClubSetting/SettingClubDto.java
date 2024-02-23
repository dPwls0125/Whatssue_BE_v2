package GDG.whatssue.dto.ClubSetting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SettingClubDto {
    private String clubName;

    private String clubInfo;

    private String clubCategory;


}
