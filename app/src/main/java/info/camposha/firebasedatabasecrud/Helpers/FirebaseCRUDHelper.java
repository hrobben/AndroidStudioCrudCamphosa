package info.camposha.firebasedatabasecrud.Helpers;

import static info.camposha.firebasedatabasecrud.Helpers.Utils.DataCache;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import info.camposha.firebasedatabasecrud.Data.MyAdapter;
import info.camposha.firebasedatabasecrud.Data.Scientist;
import info.camposha.firebasedatabasecrud.Views.ScientistsActivity;

public class FirebaseCRUDHelper {

    /**
     * This method will allow us post into firebase realtime database
     * @param a
     * @param mDatabaseRef
     * @param pb
     * @param scientist
     */
    public void insert(final AppCompatActivity a,
                          final DatabaseReference mDatabaseRef,
                          final ProgressBar pb, final Scientist scientist) {
        //check if they have passed us a valid scientist. If so then return false.
        if (scientist == null) {
            Utils.showInfoDialog(a,"VALIDATION FAILED","Scientist is null");
            return;
        } else {
            //otherwise try to push data to firebase database.
                Utils.showProgressBar(pb);
                //push data to FirebaseDatabase. Table or Child called Scientist will be
                // created.
                mDatabaseRef.child("Scientists").push().setValue(scientist).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utils.hideProgressBar(pb);

                        if(task.isSuccessful()){
                            Utils.openActivity(a, ScientistsActivity.class);
                            Utils.show(a,"Congrats! INSERT SUCCESSFUL");
                        }else{
                            Utils.showInfoDialog(a,"UNSUCCESSFUL",task.getException().
                            getMessage());
                        }
                    }

                });
        }
    }

    /**
     * This method will allow us retrieve from firebase realtime database
     * @param a
     * @param db
     * @param pb
     * @param rv
     * @param adapter
     */
    public void select(final AppCompatActivity a, DatabaseReference db,
                                         final ProgressBar pb,
                                         final RecyclerView rv,MyAdapter adapter) {
        Utils.showProgressBar(pb);

        db.child("Scientists").addValueEventListener(new ValueEventListener() {
        @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Now get Scientist Objects and populate our arraylist.
                        Scientist scientist = ds.getValue(Scientist.class);
                        scientist.setKey(ds.getKey());
                        DataCache.add(scientist);
                    }

                    adapter.notifyDataSetChanged();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.hideProgressBar(pb);
                            rv.smoothScrollToPosition(DataCache.size());
                        }
                    });
                }else {
                    Utils.show(a,"No more item found");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
                Utils.hideProgressBar(pb);
                Utils.showInfoDialog(a,"CANCELLED",databaseError.getMessage());
            }
        });
    }

    /**
     * This method will allow us update existing data in firebase realtime database
     * @param a
     * @param mDatabaseRef
     * @param pb
     * @param updatedScientist
     */
    public void update(final AppCompatActivity a,
                       final DatabaseReference mDatabaseRef,
                       final ProgressBar pb,
                       final Scientist updatedScientist) {

			if(updatedScientist == null){
				Utils.showInfoDialog(a,"VALIDATION FAILED","Scientist is null");
				return;
			}

        Utils.showProgressBar(pb);
            mDatabaseRef.child("Scientists").child(updatedScientist.getKey()).setValue(
                updatedScientist)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Utils.hideProgressBar(pb);

                            if(task.isSuccessful()){
                                Utils.show(a, updatedScientist.getName() + " Update Successful.");
                                Utils.openActivity(a, ScientistsActivity.class);
                            }else {
                                Utils.showInfoDialog(a,"UNSUCCESSFUL",task.getException().
                                getMessage());
                            }
                        }
                    });
    }


    /**
     * This method will allow us to delete from firebase realtime database
     * @param a
     * @param mDatabaseRef
     * @param pb
     * @param selectedScientist
     */
    public void delete(final AppCompatActivity a, final DatabaseReference mDatabaseRef,
                       final ProgressBar pb, final Scientist selectedScientist) {
        Utils.showProgressBar(pb);
        final String selectedScientistKey = selectedScientist.getKey();
        mDatabaseRef.child("Scientists").child(selectedScientistKey).removeValue().
        addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Utils.hideProgressBar(pb);

                if(task.isSuccessful()){
                    Utils.show(a, selectedScientist.getName() + " Successfully Deleted.");
                    Utils.openActivity(a, ScientistsActivity.class);
                }else{
                    Utils.showInfoDialog(a,"UNSUCCESSFUL",task.getException().getMessage());
                }
            }
        });

    }
}
//end

