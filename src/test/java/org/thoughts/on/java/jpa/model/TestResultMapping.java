/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thoughts.on.java.jpa.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.thoughts.on.java.jpa.value.BookValue;

/**
 *
 * @author Thorben
 */
@RunWith(Arquillian.class)
public class TestResultMapping {

    @PersistenceContext
    private EntityManager em;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(Author.class, Book.class, BookValue.class)
                .addAsManifestResource("META-INF/persistence.xml",
                        "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/orm.xml",
                        "orm.xml");
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithoutMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT a.id, a.firstName, a.lastName, a.version FROM Author a").getResultList();
        results.stream().forEach((record) -> {
            Long id = ((BigInteger) record[0]).longValue();
            String firstName = (String) record[1];
            String lastName = (String) record[2];
            Integer version = (Integer) record[3];
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorEntityDefaultMapping() {
        List<Author> results = this.em.createNativeQuery("SELECT a.id, a.firstName, a.lastName, a.version FROM Author a", Author.class).getResultList();
        results.stream().forEach((author) -> {
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorEntityCustomMapping() {
        List<Author> results = this.em.createNativeQuery("SELECT a.id as authorId, a.firstName, a.lastName, a.version FROM Author a", "AuthorMapping").getResultList();
        results.stream().forEach((author) -> {
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorEntityCustomXmlMapping() {
        List<Author> results = this.em.createNativeQuery("SELECT a.id as authorId, a.firstName, a.lastName, a.version FROM Author a", "AuthorMappingXml").getResultList();
        results.stream().forEach((author) -> {
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorBookEntityMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT b.id, b.title, b.author_id, b.version, a.id as authorId, a.firstName, a.lastName, a.version as authorVersion FROM Book b JOIN Author a ON b.author_id = a.id", "BookAuthorMapping").getResultList();
        results.stream().forEach((record) -> {
            Book book = (Book) record[0];
            Author author = (Author) record[1];
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "]");
            System.out.println("Book: ID [" + book.getId() + "] title[" + book.getTitle() + "] author [" + book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorBookEntityXmlMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT b.id, b.title, b.author_id, b.version, a.id as authorId, a.firstName, a.lastName, a.version as authorVersion FROM Book b JOIN Author a ON b.author_id = a.id", "BookAuthorMappingXml").getResultList();
        results.stream().forEach((record) -> {
            Book book = (Book) record[1];
            Author author = (Author) record[0];
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "]");
            System.out.println("Book: ID [" + book.getId() + "] title[" + book.getTitle() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorBookCountMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT a.id, a.firstName, a.lastName, a.version, count(b.id) as bookCount FROM Book b JOIN Author a ON b.author_id = a.id GROUP BY a.id, a.firstName, a.lastName, a.version", "AuthorBookCountMapping").getResultList();
        results.stream().forEach((record) -> {
            Author author = (Author) record[0];
            Long bookCount = (Long) record[1];
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "] number of books [" + bookCount + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithAuthorBookCountXmlMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT a.id, a.firstName, a.lastName, a.version, count(b.id) as bookCount FROM Book b JOIN Author a ON b.author_id = a.id GROUP BY a.id, a.firstName, a.lastName, a.version", "AuthorBookCountMappingXml").getResultList();
        results.stream().forEach((record) -> {
            Author author = (Author) record[0];
            Long bookCount = (Long) record[1];
            System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "] number of books [" + bookCount + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithBookValueMapping() {
        List<BookValue> results = this.em.createNativeQuery("SELECT b.id, b.title, b.version, a.firstName || a.lastName as authorName FROM Book b JOIN Author a ON b.author_id = a.id", "BookValueMapping").getResultList();
        results.stream().forEach((book) -> {;
            System.out.println("Book: ID [" + book.getId() + "] title [" + book.getTitle() + "] authorName [" + book.getAuthorName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithBookValueAndEntityMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT b.id, b.title, b.version, a.firstName || a.lastName as authorName, b.author_id FROM Book b JOIN Author a ON b.author_id = a.id", "BookValueAndEntityMapping").getResultList();
        results.stream().forEach((Object[] record) -> {
            Book entity = (Book) record[0];
            BookValue book = (BookValue) record[1];
            System.out.println(entity);
            System.out.println("Book: ID [" + book.getId() + "] title [" + book.getTitle() + "] authorName [" + book.getAuthorName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithBookValueXmlMapping() {
        List<BookValue> results = this.em.createNativeQuery("SELECT b.id, b.title, b.version, a.firstName || a.lastName as authorName FROM Book b JOIN Author a ON b.author_id = a.id", "BookValueMappingXml").getResultList();
        results.stream().forEach((book) -> {
            System.out.println("Book: ID [" + book.getId() + "] title [" + book.getTitle() + "] authorName [" + book.getAuthorName() + "]");
        });
    }

    @Test
    @UsingDataSet("data/testData.yml")
    public void queryWithBookValueStreamMapping() {
        List<Object[]> results = this.em.createNativeQuery("SELECT b.id, b.title, b.version, a.firstName || a.lastName as authorName FROM Book b JOIN Author a ON b.author_id = a.id").getResultList();
        List<BookValue> books = new ArrayList<>(results.size());
        results.stream().forEach((record) -> books.add(new BookValue(((BigInteger) record[0]).longValue(), (String) record[1], ((Integer) record[2]).longValue(), (String) record[3])));

        books.stream().forEach((book) -> {
            System.out.println("Book: ID [" + book.getId() + "] title [" + book.getTitle() + "] authorName [" + book.getAuthorName() + "]");
        });
    }
}
