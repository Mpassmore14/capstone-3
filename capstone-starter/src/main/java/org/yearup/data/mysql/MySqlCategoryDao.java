package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        return null;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try
                (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        {
            // get category by id
            return null;
        }
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO category(name,category_id, description )" +
                " VALUES (?, ?,?)";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setInt(2, category.getCategoryId());
            statement.setString(3, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        String sql = "UPDATE category" +
                " SET name = ? " +
                "    category_id = ? " +
                "    description = ? " +
                " WHERE category_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category.getName());
            statement.setInt(2, category.getCategoryId());
            statement.setString(3, category.getDescription());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // update category
        }
    }

    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM products WHERE category_id = ?;";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }


        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}


