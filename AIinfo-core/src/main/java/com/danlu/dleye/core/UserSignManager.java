package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserSign;

public interface UserSignManager {

    int addUserSign(UserSign userSign);

    UserSign getUserSignById(Long id);

    List<UserSign> getUserSignListByParams(Map<String, Object> map);

    int updateUserSign(UserSign userSign);

    int deletUserSign(Long id);

}
