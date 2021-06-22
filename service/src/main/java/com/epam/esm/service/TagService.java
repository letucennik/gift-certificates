package com.epam.esm.service;

import com.epam.esm.entity.Tag;

public interface TagService {

    Tag create(Tag tag);

    Tag read(long id);

    void delete(long id);
}
