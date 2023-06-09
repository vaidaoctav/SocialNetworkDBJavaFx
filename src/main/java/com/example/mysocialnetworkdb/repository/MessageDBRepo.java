package com.example.mysocialnetworkdb.repository;

import com.example.mysocialnetworkdb.domain.Message;
import com.example.mysocialnetworkdb.exceptions.RepositoryException;
import com.example.mysocialnetworkdb.utils.DBAccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepo implements Repository<Message,Integer> {
    private final DBAccess dbAccess = new DBAccess();

    public List<Message> findAllFromUsers(Integer id_user1, Integer id_user2) {
        List<Message> list = new ArrayList();
        String sql = "SELECT * FROM messages WHERE id_user1 =? and id_user2=? ";
        Message messagee = null;

        try (Connection connection = dbAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id_user1);
            preparedStatement.setInt(2, id_user2);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idMessage = resultSet.getInt("id");
                int idUser1 = resultSet.getInt("id_user1");
                int idUser2 = resultSet.getInt("id_user2");
                int idSender = resultSet.getInt("id_sender");
                String msg = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("datetime").toLocalDateTime();

                messagee = new Message(idMessage, idUser1, idUser2, idSender, msg, date);
                list.add(messagee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return list;
    }


    @Override
    public Message find(Integer id) throws RepositoryException {
        String sql = "SELECT * FROM messages WHERE id =?";
        Message message = null;

        try (Connection connection = dbAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idMessage = resultSet.getInt("id");
                int idUser1 = resultSet.getInt("id_user1");
                int idUser2 = resultSet.getInt("id_user2");
                int idSender = resultSet.getInt("id_sender");
                String msg = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("datetime").toLocalDateTime();

                message = new Message(idMessage, idUser1, idUser2, idSender, msg, date);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();

        //open connection to DB
        //if exception is thrown, connection is lost
        try (Connection connection = dbAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int idMessage = resultSet.getInt("id");
                int idUser1 = resultSet.getInt("id_user1");
                int idUser2 = resultSet.getInt("id_user2");
                int idSender = resultSet.getInt("id_sender");
                String msg = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("datetime").toLocalDateTime();

                Message message = new Message(idMessage, idUser1, idUser2, idSender, msg, date);
                messages.add(message);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message message) {
        String sql = "INSERT INTO messages(id,id_user1,id_user2,id_sender,message,datetime) VALUES(?,?,?,?,?,?)";
        try (Connection connection = dbAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, message.getId());
            preparedStatement.setInt(2, message.getId_user1());
            preparedStatement.setInt(3, message.getId_user2());
            preparedStatement.setInt(4, message.getId_sender());
            preparedStatement.setString(5, message.getMessage());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(message.getDateTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Message delete(Message message) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = dbAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, message.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message update(Message updateEntity) {
        return null;
    }
}
