/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import static java.util.Objects.*;

import com.aaron.desktop.model.log.LogManager;
import com.aaron.desktop.constant.Resource;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Aaron
 */
@Deprecated
public final class HibernateUtil 
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

    public static Session getInstance()
    {
        try
        {
            if(isNull(sessionFactory))
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

    public static Boolean wrapInTransaction(Function<Session, Boolean> databaseTransaction, Consumer<JDBCException> databaseExceptionAction)
    {
        Session session = HibernateUtil.getInstance();
        Boolean result = Boolean.FALSE;

        if(nonNull(session))
        {
            Transaction transaction = session.beginTransaction();
            try
            {      
                result = databaseTransaction.apply(session);
                transaction.commit();
            }
            catch(final JDBCException e)
            {
                transaction.rollback();
                databaseExceptionAction.accept(e);
            }
            finally
            {
                session.close();
            }
        }
        
        return result;
    }
    
    public static void close()
    {
        if(nonNull(sessionFactory))
        {
            sessionFactory.close();
        }
    }
}
