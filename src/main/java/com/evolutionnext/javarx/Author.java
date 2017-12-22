package com.evolutionnext.javarx;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Author {

    private final List<Book> books;
    private final String lastName;
    private final String firstName;

    public Author(String firstName, String lastName, List<Book> books) {
      this.books = books;
      this.lastName = lastName;
      this.firstName = firstName;
    }

    public Author(String firstName, String lastName, Book... books) {
        this(firstName, lastName, Arrays.asList(books));
    }

    public List<Book> getBooks() {
        return books;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Author{");
        sb.append("books=").append(books);
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(lastName, author.lastName) &&
                Objects.equals(firstName, author.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName) % 81;
    }
}
