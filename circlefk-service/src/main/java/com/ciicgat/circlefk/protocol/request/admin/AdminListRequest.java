package com.ciicgat.circlefk.protocol.request.admin;

import com.ciicgat.circlefk.protocol.request.PageQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminListRequest extends PageQuery {
    private List<Integer> idNotInList;
    private String username;
    private String usernameSearch;
    private String name;
    private String nameSearch;
    private Integer status;
    private String mobile;
    private String email;
    private Long enterpriseId;
    private String timeModifiedStart;
    private String timeModifiedEnd;
}
