package com.celera.mongo.repo;

import org.springframework.data.repository.CrudRepository;

import com.celera.mongo.entity.Address;

public interface AddressRepo extends CrudRepository<Address, Long>
{
}
