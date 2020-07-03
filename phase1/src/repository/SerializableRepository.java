package repository;

import notification.support.Ticket;

import java.io.*;
import java.util.*;

public class SerializableRepository<T extends Serializable> implements Repository<T> {

    public static void main(String[] args) {
        Repository<Ticket> ticketRepo =
                new CSVRepository<>("a.csv",Ticket::new);
        // Ticket ticket = new Ticket("123", Ticket.Category.ACCOUNT);
        // ticketRepo.add(ticket);

        // ticketRepo.save();
        System.out.println(ticketRepo.get(0).getCategory());
        //repository.add(ticket);
        //repository.save();

        //System.out.println(ticketRepo.get(0).get("content"));

        //Ticket ticket = new Ticket();
        //ticket.setContent("aha");
        //ticketRepository.addTicket(ticket);
        //ticketRepository.save();
    }

    private List<T> data;

    private final String path;

    public SerializableRepository(String path) {
        data = new ArrayList<>();
        this.path = path;
    }

    @SuppressWarnings("unchecked")
    public void read(){
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            data = (List<T>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void save(){
        try {
            OutputStream file = new FileOutputStream(path);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            // serialize the
            output.writeObject(data);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T data) {
        this.data.add(data);
    }

    public T get(int id) {
        return data.get(id);
    }
}
