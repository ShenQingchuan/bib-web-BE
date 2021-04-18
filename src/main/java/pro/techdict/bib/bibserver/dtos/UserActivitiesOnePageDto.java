package pro.techdict.bib.bibserver.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserActivitiesOnePageDto {
  int pageTotal;
  List<UserActivityDto> activities;
}
