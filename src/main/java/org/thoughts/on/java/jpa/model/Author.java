package org.thoughts.on.java.jpa.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import org.thoughts.on.java.jpa.value.BookValue;

@Entity
@SqlResultSetMappings({
    @SqlResultSetMapping(
            name = "BookAuthorMapping",
            entities = {
                @EntityResult(
                        entityClass = Book.class,
                        fields = {
                            @FieldResult(name = "id", column = "id"),
                            @FieldResult(name = "title", column = "title"),
                            @FieldResult(name = "author", column = "author_id"),
                            @FieldResult(name = "version", column = "version")}),
                @EntityResult(
                        entityClass = Author.class,
                        fields = {
                            @FieldResult(name = "id", column = "authorId"),
                            @FieldResult(name = "firstName", column = "firstName"),
                            @FieldResult(name = "lastName", column = "lastName"),
                            @FieldResult(name = "version", column = "authorVersion")})}),
    @SqlResultSetMapping(
            name = "AuthorMapping",
            entities = @EntityResult(
                    entityClass = Author.class,
                    fields = {
                        @FieldResult(name = "id", column = "authorId"),
                        @FieldResult(name = "firstName", column = "firstName"),
                        @FieldResult(name = "lastName", column = "lastName"),
                        @FieldResult(name = "version", column = "version")})),
    @SqlResultSetMapping(
            name = "AuthorBookCountMapping",
            entities = @EntityResult(
                    entityClass = Author.class,
                    fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "firstName", column = "firstName"),
                        @FieldResult(name = "lastName", column = "lastName"),
                        @FieldResult(name = "version", column = "version")}),
            columns = @ColumnResult(name = "bookCount", type = Long.class)),
    @SqlResultSetMapping(
            name = "BookValueMapping",
            classes = @ConstructorResult(
                    targetClass = BookValue.class,
                    columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "title"),
                        @ColumnResult(name = "version", type = Long.class),
                        @ColumnResult(name = "authorName")})),
    @SqlResultSetMapping(
            name = "BookValueAndEntityMapping",
            entities = {
                @EntityResult(
                        entityClass = Book.class,
                        fields = {
                            @FieldResult(name = "id", column = "id"),
                            @FieldResult(name = "title", column = "title"),
                            @FieldResult(name = "author", column = "author_id"),
                            @FieldResult(name = "version", column = "version")})},
            classes = @ConstructorResult(
                    targetClass = BookValue.class,
                    columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "title"),
                        @ColumnResult(name = "version", type = Long.class),
                        @ColumnResult(name = "authorName")}))
})
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version")
    private int version;

    @Column
    private String firstName;

    @Column
    private String lastName;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Author)) {
            return false;
        }
        Author other = (Author) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (firstName != null && !firstName.trim().isEmpty()) {
            result += "firstName: " + firstName;
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            result += ", lastName: " + lastName;
        }
        return result;
    }
}
