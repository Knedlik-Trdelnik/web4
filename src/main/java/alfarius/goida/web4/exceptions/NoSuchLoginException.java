package alfarius.goida.web4.exceptions;

public class NoSuchLoginException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Такого логина не существует(";
    }
}
