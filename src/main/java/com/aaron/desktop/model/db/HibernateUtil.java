/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import com.aaron.desktop.model.others.Resource;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Aaron
 */
public class HibernateUtil 
{
    private static final SessionFactory sessionFactory;

    static
    {
        final Configuration cfg = new Configuration();
        cfg.configure(HibernateUtil.class.getResource(Resource.HIBERNATE_CONFIG));

        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    private HibernateUtil()
    {}

    @SuppressWarnings("unchecked")
    public static <T> List<T> listAndCast(final Query q)
    {
        List<T> list = q.list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> listAndCast(final Criteria q)
    {
        List<T> list = q.list();
        return list;
    }

    public static SessionFactory getInstance()
    {
        return sessionFactory;
    }

    public static void close()
    {
        sessionFactory.close();
    }
}
