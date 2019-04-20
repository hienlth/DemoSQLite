package info.hienlth.qlsv;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String DbName = "QLSV.db";
    SQLiteDatabase db = null;
    EditText txtMa, txtTen, txtSiSo;
    ListView lvDanhSach;
    Button btnThem, btnTim;

    ArrayList<Lop> dsLop = new ArrayList<Lop>();
    ArrayAdapter<Lop> lopArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //chạy 1 lần tạo CSDL, tạo bảng Lop
        CreateDatabase();
//        CreateTableLop();

        //mapping control
        txtMa = (EditText)findViewById(R.id.txtMaLop);
        txtTen = (EditText)findViewById(R.id.txtTenLop);
        txtSiSo = (EditText)findViewById(R.id.txtSiSo);
        lvDanhSach = (ListView) findViewById(R.id.lvDanhSach);
        btnThem = (Button) findViewById(R.id.btnThemLop);
        btnTim = (Button) findViewById(R.id.btnTimKiem);

        //gắn dữ liệu cho ListView
        lopArrayAdapter = new ArrayAdapter<Lop>(
            MainActivity.this,
                android.R.layout.simple_list_item_1,
                dsLop
        );
        lvDanhSach.setAdapter(lopArrayAdapter);

        loadDBToListView();

        //xử lý nút thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tạo
                Lop lop = new Lop(
                    txtMa.getText().toString(),
                    txtTen.getText().toString(),
                    Integer.parseInt(txtSiSo.getText().toString())
                );
                //thêm vào database
                AddLop(lop);
                //update
                loadDBToListView();

                //reset textbox
            }
        });

        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsLop.clear();
                Cursor c = db.query("Lop", null,
                        "MaLop = ?",
                        new String[]{txtMa.getText().toString()},
                        null, null, null);
                c.moveToFirst();//chuyển về record đầu tiên
                String data = "";
                while(c.isAfterLast() == false)
                {
                    dsLop.add(new Lop(c.getString(0).toString(),
                            c.getString(1).toString(),
                            c.getInt(2)));
                    c.moveToNext();
                }
                //Toast.makeText(this, data, Toast.LENGTH_LONG).show();
                c.close();
                lopArrayAdapter.notifyDataSetChanged();
            }
        });

    }//end OnCreate()

    public void CreateDatabase()
    {
        db = openOrCreateDatabase(DbName, MODE_PRIVATE, null);
    }

    public  void CloseDatabase()
    {
        if(db.isOpen())
            db.close();
    }

    public void DeleteDatabase()
    {
        String thongbao = "";
        if(deleteDatabase(DbName))
        {
            thongbao = "Đã xóa thành công database " + DbName;
        }
        else
        {
            thongbao = "Không thể xóa QLSV.db!";
        }
        Toast.makeText(MainActivity.this, thongbao, Toast.LENGTH_SHORT).show();
    }

    public  void CreateTableLop()
    {
        String sql = "CREATE TABLE Lop(MaLop  TEXT PRIMARY KEY, "
                + "TenLop TEXT, siso INTEGER) ";
        db.execSQL(sql);
    }

    public void AddLop(Lop lop)
    {
        ContentValues values = new ContentValues();
        try {
            values.put("MaLop", lop.getMaLop());
            values.put("TenLop", lop.getTenLop());
            values.put("SiSo", lop.getSiSo());
        }catch (Exception ex)
        {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("LoiThemLop", ex.getMessage());
        }

        if(db.insert("Lop", null, values) == -1)
            Toast.makeText(MainActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
    }

    public void UpdateLop(Lop lop)
    {
        ContentValues values = new ContentValues();
        values.put("TenLop", lop.getTenLop());
        values.put("SiSo", lop.getSiSo());

        db.update("Lop", values,
                "MaLop=?", new String[]{lop.getMaLop()});
    }

    public void XoaLop(String MaLop)
    {
        //Xóa tất cả
        if(MaLop == null)
            db.delete("Lop", null, null);
        else
            db.delete("Lop", "MaLop=?", new String[]{MaLop});
    }

    //load database to ListView
    public  void loadDBToListView()
    {
        dsLop.clear();
        Cursor c = db.query("Lop", null, null, null, null, null, null);
        c.moveToFirst();//chuyển về record đầu tiên
        String data = "";
        while(c.isAfterLast() == false)
        {
            dsLop.add(new Lop(c.getString(0).toString(),
                    c.getString(1).toString(),
                    c.getInt(2)));
            c.moveToNext();
        }
        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        c.close();
        lopArrayAdapter.notifyDataSetChanged();
    }

}
