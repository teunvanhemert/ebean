package com.avaje.tests.model.basic.xtra;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.avaje.ebean.config.PersistBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.TxType;
import com.avaje.ebean.annotation.Transactional;

import static org.junit.Assert.assertEquals;

public class DummyDao {

  Logger logger = LoggerFactory.getLogger(DummyDao.class);
  
  @Transactional(type = TxType.REQUIRES_NEW)
  public void doSomething() {
    
    logger.info("  --- in DummyDao.doSomething() with TxType.REQUIRES_NEW");
    Transaction txn = Ebean.currentTransaction();
    if (txn == null) {
      logger.error("  NO TRANSACTION ??");
    } else {
      logger.info("  --- txn - "+txn);  
    }
    
  }

  @Transactional
  public void addToObject(Long id, Double anotherNumber, List<Long> ids) throws EntityNotFoundException {
    // and more code
  }


  @Transactional(batch = PersistBatch.ALL, batchOnCascade = PersistBatch.ALL, batchSize = 99)
  public void doWithBatchOptionsSet() {

    Transaction txn = Ebean.currentTransaction();
    assertEquals(PersistBatch.ALL, txn.getBatch());
    assertEquals(PersistBatch.ALL, txn.getBatchOnCascade());
    assertEquals(99, txn.getBatchSize());
  }


  @Transactional(batch = PersistBatch.INSERT, batchOnCascade = PersistBatch.NONE, batchSize = 77)
  public void doOuterWithBatchOptionsSet() {

    Transaction txn = Ebean.currentTransaction();

    assertEquals(PersistBatch.INSERT, txn.getBatch());
    assertEquals(PersistBatch.NONE, txn.getBatchOnCascade());
    assertEquals(77, txn.getBatchSize());

    doWithBatchOptionsSet();

    // batch options set back
    assertEquals(PersistBatch.INSERT, txn.getBatch());
    assertEquals(PersistBatch.NONE, txn.getBatchOnCascade());
    assertEquals(77, txn.getBatchSize());

  }

}
