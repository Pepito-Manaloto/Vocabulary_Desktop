/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import com.aaron.desktop.constant.Config;
import com.aaron.desktop.main.PropertiesConfig;
import com.aaron.desktop.model.log.LogManager;
import static com.aaron.desktop.model.others.EncryptionUtil.decrypt;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.cfg.Environment;

/**
 *
 * @author Aaron
 */
public final class JPAUtil
{
    private final static LogManager logger = LogManager.getInstance();
    private final static String className = JPAUtil.class.getSimpleName();

    private static final String PERSISTENCE_UNIT_NAME = "VocabularyDB";
    private static final EntityManagerFactory EM_FACTORY;
    private static EntityManager em;

    static
    {
        EM_FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, loadEntityManagerFactoryProperties());
    }

    public static Map<String, Object> loadEntityManagerFactoryProperties()
    {
        try
        {
            Map<String, Object> settings = new HashMap<>();
            settings.put(Environment.URL, decrypt(PropertiesConfig.getProperty(Config.DB_URL)));
            settings.put(Environment.USER, decrypt(PropertiesConfig.getProperty(Config.DB_USER)));
            settings.put(Environment.PASS, decrypt(PropertiesConfig.getProperty(Config.DB_PASS)));
            settings.put(Environment.SHOW_SQL, PropertiesConfig.getProperty(Config.HIBERNATE_DEBUG));
            settings.put(Environment.FORMAT_SQL, PropertiesConfig.getProperty(Config.HIBERNATE_DEBUG));

            return settings;
        }
        catch (Exception ex)
        {
            logger.fatal(className, "loadEntityManagerFactoryProperties", "Failed decrypting database properties(url, user, pw)", ex);
            System.exit(1);
        }

        return null;
    }

    public static EntityManager getEntityManager()
    {
        if(isNull(em) || !em.isOpen())
        {
            em = EM_FACTORY.createEntityManager();
        }

        return em;
    }
    
    public static void closeEntityManager()
    {
        if(nonNull(em) && em.isOpen())
        {
            em.close();
        }
    }
    
    public static void transact(Consumer<EntityManager> databaseTransaction, Consumer<Exception> databaseExceptionAction)
    {
        if (em.getTransaction().isActive())
        {
            databaseTransaction.accept(em);
        }
        else
        {
            try 
            {
                em.getTransaction().begin();
                databaseTransaction.accept(em);
                em.getTransaction().commit();
            }
            catch (Exception e)
            {
                databaseExceptionAction.accept(e);
            }
        }
    }
    
    public static <T> Optional<T> transactAndReturn(Function<EntityManager, Optional<T>> databaseTransaction, Consumer<Exception> databaseExceptionAction)
    {
        if (em.getTransaction().isActive())
        {
            return databaseTransaction.apply(em);
        }
        else
        {
            try 
            {
                em.getTransaction().begin();
                Optional<T> result = databaseTransaction.apply(em);
                em.getTransaction().commit();
                return result;
            }
            catch (Exception e)
            {
                databaseExceptionAction.accept(e);
            }
        }
        return Optional.empty();
    }

    public static void shutdown()
    {
        if(EM_FACTORY != null)
        {
            EM_FACTORY.close();
        }
    }
}
