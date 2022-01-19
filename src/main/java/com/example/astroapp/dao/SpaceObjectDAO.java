package com.example.astroapp.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
@Transactional
public class SpaceObjectDAO extends JdbcDaoSupport {

//    @Autowired
//    DataSource dataSource;

    public long saveObject(String catalogId, String name, String catalog,
                           Float catalogDec, Float catalogRec, Float catalogMag) {
        assert getJdbcTemplate() != null;
        String insertQuery = "INSERT INTO public.object " +
                "(id, name, catalog, catalog_id, catalog_rec, catalog_dec, catalog_mag)" +
                "VALUES (public.object_id_seq.nextval ,?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{ "id" });
            ps.setString(1, catalogId);
            ps.setString(2, name);
            ps.setString(3, catalog);
            ps.setFloat(4, catalogDec);
            ps.setFloat(5, catalogRec);
            ps.setFloat(6, catalogMag);
            return ps;
                }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public long getObjectByCatId(String catalogId) {
        assert getJdbcTemplate() != null;
        return Objects.requireNonNull(getJdbcTemplate().queryForObject("SELECT id FROM public.object " +
                "WHERE catalog_id=?", Long.class, catalogId));
    }


}
