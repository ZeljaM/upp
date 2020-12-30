package com.upp.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo
{

    private String process;

    private String task;

    private String title;

    private Date createdAt;

}
