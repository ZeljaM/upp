package com.upp.dtos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostFormRequest implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1067335988682230059L;

    private String task;

    private String process;

    private Map< String, String > fields = new HashMap<>();

}
