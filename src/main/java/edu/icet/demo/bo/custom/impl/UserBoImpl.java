package edu.icet.demo.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.bo.custom.UserBo;
import edu.icet.demo.dao.DaoFactory;
import edu.icet.demo.dao.custom.UserDao;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.model.User;
import edu.icet.demo.util.DaoType;
import org.apache.commons.codec.digest.DigestUtils;

public class UserBoImpl implements UserBo {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final UserDao userDao = DaoFactory.getInstance().getDao(DaoType.USER);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public User authenticate(String usernameOrEmail, String password) {
        ensureDefaultAdminExists();

        UserEntity entity = userDao.searchByUsernameOrEmail(usernameOrEmail.trim());
        if (entity == null) {
            return null;
        }
        String hash = DigestUtils.sha256Hex(password);
        if (!hash.equalsIgnoreCase(entity.getPassword())) {
            return null;
        }
        return mapper.convertValue(entity, User.class);
    }

    @Override
    public boolean addUser(User user) {
        UserEntity entity = mapper.convertValue(user, UserEntity.class);
        entity.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        if (entity.getRole() == null || entity.getRole().isBlank()) {
            entity.setRole("EMPLOYEE");
        }
        return userDao.insert(entity);
    }

    @Override
    public String generateUserId() {
        String latestId = userDao.getLatestId();
        if (latestId == null) {
            return "U0001";
        }
        int number = Integer.parseInt(latestId.substring(1));
        return String.format("U%04d", number + 1);
    }

    /**
     * Seeds the default shop-owner account on first run so the owner can
     * always log in (also seeded by database/schema.sql).
     */
    private void ensureDefaultAdminExists() {
        if (userDao.count() == 0) {
            userDao.insert(new UserEntity(
                    "U0001",
                    DEFAULT_ADMIN_USERNAME,
                    "owner@clothingstore.local",
                    DigestUtils.sha256Hex(DEFAULT_ADMIN_PASSWORD),
                    "ADMIN",
                    "Clothing Store HQ"));
        }
    }
}
