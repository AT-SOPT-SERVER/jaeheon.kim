package org.sopt.util;

import org.sopt.domain.Post;

import static org.sopt.constant.PostConstant.DELIMITER;

public class PostUtil {

    public static Post parsePost(String line){
        String[] column = line.split(DELIMITER);
        Long id = Long.parseLong(column[0]);
        String title = column[1];
        return new Post(id, title);
    }
}
