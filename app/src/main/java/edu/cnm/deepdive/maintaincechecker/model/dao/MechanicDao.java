package edu.cnm.deepdive.maintaincechecker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.maintaincechecker.model.entity.Maintenance;
import edu.cnm.deepdive.maintaincechecker.model.entity.Mechanic;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface MechanicDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(Mechanic maintenance);

  @Insert(onConflict =  OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Mechanic> mechanics);

  @Update
  Single<Integer> update(Mechanic... mechanics);

  @Delete
  Single<Integer> delete(Mechanic... mechanics);

  @Query("SELECT * FROM Mechanic ORDER BY name")
  LiveData<List<Mechanic>> selectAll();
}
