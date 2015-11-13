/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import com.aaron.desktop.model.log.LogManager;
import com.aaron.desktop.model.others.Resource;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Aaron
 */
public class HibernateUtil 
{
    private static SessionFactory sessionFactory;
    private static final LogManager logger = LogManager.getInstance();
    private static final String className = HibernateUtil.class.getSimpleName();
    private static final Configuration cfg;
    private static final StandardServiceRegistryBuilder builder;

    static
    {
        cfg = new Configuration();
        cfg.configure(HibernateUtil.class.getResource(Resource.HIBERNATE_CONFIG));

        builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());
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

    public static Session getInstance()
    {
        try
        {
            if(sessionFactory == null)
            {
                sessionFactory = cfg.buildSessionFactory(builder.build());
            }

            Session session = sessionFactory.openSession();
        
            return session;
        }
        catch(HibernateException ex)
        {
            logger.error(className, "getInstance()", ex.getMessage(), ex);
            sessionFactory = null; // Just to make sure that the object is not initialize in an error state
            return null;
        }
    }

    public static void close()
    {
        sessionFactory.close();
    }
}
