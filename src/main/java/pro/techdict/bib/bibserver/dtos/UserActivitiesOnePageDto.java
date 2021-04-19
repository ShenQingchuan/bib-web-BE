package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivitiesOnePageDto {
  int pageTotal;
  List<UserActivityDto> activities;
}
