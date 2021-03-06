package edu.cnm.deepdive.maintaincechecker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.maintaincechecker.model.entity.Maintenance;
import edu.cnm.deepdive.maintaincechecker.model.entity.Mechanic;
import edu.cnm.deepdive.maintaincechecker.model.pojo.MaintenanceWithMechanic;
import edu.cnm.deepdive.maintaincechecker.service.MaintenanceRepository;
import edu.cnm.deepdive.maintaincechecker.service.MechanicRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MaintenanceRepository maintenanceRepository;
  private final MechanicRepository mechanicRepository;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final MutableLiveData<MaintenanceWithMechanic> maintenance;
  private final MutableLiveData<Boolean> permissionsChecked;

  public MainViewModel(@NonNull Application application) {
    super(application);
    maintenanceRepository = new MaintenanceRepository(application);
    mechanicRepository = new MechanicRepository(application);
    permissionsChecked = new MutableLiveData<>(false);
    maintenance = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();

  }

  public LiveData<List<MaintenanceWithMechanic>> getMaintenanceHistory() {
    return maintenanceRepository.getAll();
  }

  public LiveData<List<Mechanic>> getMechanics() {
    return mechanicRepository.getAll();
  }

  public LiveData<MaintenanceWithMechanic> getMaintenance() {
    return maintenance;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<Boolean> getPermissionsChecked() {
    return permissionsChecked;
  }

  public void setPermissionsChecked(boolean checked) {
    permissionsChecked.setValue(checked);
  }

  public void setMaintenanceId(long id) {
    throwable.setValue(null);
    pending.add(
        maintenanceRepository.get(id)
            .subscribe(
                this.maintenance::postValue,
                this.throwable::postValue
            )
    );
  }

  public void save(Maintenance maintenance) {
    throwable.setValue(null);
    pending.add(
        maintenanceRepository.save(maintenance)
            .subscribe(
                () -> {},
                this.throwable::postValue
            )
    );
  }

  public void save(Maintenance maintenance, String mechanicName) {
    throwable.setValue(null);
    pending.add(
        maintenanceRepository.save(maintenance, mechanicName)
            .subscribe(
                () -> {},
                this.throwable::postValue
            )
    );
  }

@OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
}
}
