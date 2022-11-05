package com.sam.util.service;

import java.io.IOException;
import java.util.List;

interface Droppable {

    List<String> getAllUsers() throws IOException;

    void getCurrentUserInfo(String userName) throws IOException;

    void refreshCurrentUserPassword(String userName, String newPassword) throws IOException;
}
