package com.ug.smartcampus.service;

import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.ResourceDao;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.model.Resource;
import java.sql.SQLException;
import java.util.List;

/** Boundary between JDBC DAOs and the in-memory services used by the application. */
public final class PersistenceService {
    private final RequestDao requestDao;
    private final ResourceDao resourceDao;

    public PersistenceService(RequestDao requestDao, ResourceDao resourceDao) {
        this.requestDao = requestDao;
        this.resourceDao = resourceDao;
    }

    public List<Request> loadRequests() throws SQLException { return requestDao.findAll(); }
    public List<Resource> loadResources() throws SQLException { return resourceDao.findAll(); }
}
