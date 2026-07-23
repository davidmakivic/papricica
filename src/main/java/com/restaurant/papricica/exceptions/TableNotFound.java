package com.restaurant.papricica.exceptions;

public class TableNotFound extends AppException{

    public TableNotFound(){
        super(ErrorCode.TABLE_NOT_FOUND, "The provided table could not be found.");
    }

}
