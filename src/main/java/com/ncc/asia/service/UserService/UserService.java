package com.ncc.asia.service.UserService;

import com.ncc.asia.dto.user.UserDTODisplay;
import com.ncc.asia.dto.user.UserDTOModify;
import com.ncc.asia.dto.user.UserPageResponse;
import com.ncc.asia.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    // CRUD
    // Find by id
    UserDTODisplay findById(int id);

    // Find all
    List<UserDTODisplay> findAll ();

    // Save
    UserDTODisplay save (UserDTOModify accountUserDTO);

    // Delete by id
    void deleteById (int id);

    // Find by username
    User findByName(String username);

    // Find employee based on some filter
    Page<UserDTODisplay> findUserBySomeFilter (Boolean enabled, String branch, String username,
                                               Integer pageNo, Integer pageSize);

    // Find user joined open talk sort by ascending the number of joined open talk
    Page<UserDTODisplay> findUserBySortByNoOfOpenTalk (Boolean enabled, String branch, String username,
                                                       Integer pageNo, Integer pageSize);

    // Register host open talk
    void registerHost(Integer idHost, Integer idOpenTalk);

    // Register joined open talk
    void registerJoinedOpenTalk(Integer idHost, Integer idOpenTalk);

    // Find user have not hosted open talk in this year, or have not hosted any open talk
    Page<UserDTODisplay> findUserHaveNotHostedOpenTalk (Integer pageNo, Integer pageSize);

    // Find random user for host open talk
    UserDTODisplay findRandomUserForHost ();
}
