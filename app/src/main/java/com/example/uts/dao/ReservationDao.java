/*ackage com.example.uts.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.uts.model.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {
    @Query("SELECT * FROM reservations")
    List<Reservation> getAll();

    @Insert
    void addReservation(Reservation reservation);

    @Delete
    void deleteReservation(Reservation reservation);

    @Query("SELECT * FROM reservations where user_id = :user_id")
    List<Reservation> getReservationsByUserId(int user_id);
}
*/