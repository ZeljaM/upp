package com.upp.dtos;

import java.util.Comparator;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResultItemDTO implements Comparator< ResultItemDTO >
{

    private List< PaperDTO > papers;

    private int partOfPage;

    private String textToShow;

    @Override
    public int compare( ResultItemDTO resultItem1, ResultItemDTO resultItem2 )
    {
        if ( resultItem1.getPartOfPage() < resultItem2.getPartOfPage() )
        {
            return -1;
        }
        else
        {
            return 1;
        }

    }

}
