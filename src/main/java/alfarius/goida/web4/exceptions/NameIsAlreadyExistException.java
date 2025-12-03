package alfarius.goida.web4.exceptions;

public class NameIsAlreadyExistException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Имя занято";
    }
}
