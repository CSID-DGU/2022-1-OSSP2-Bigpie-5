package kr.co.bigpie.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{


    private Activity mcontext;

    // Memo 객체를 담을 MemoItem / 작성일/제목/내용 이 담긴다
    private ArrayList<MemoItem> items = new ArrayList<>();


    public MemoAdapter(Activity context) {
        mcontext = context;
    }

    // 리사이클러뷰에 데이터 추가 메소드
    public void addItem(MemoItem item) {
        items.add(item);
    }

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        // 미리 만들어 놓은 item_rv_memo.xml 기입
        View view = inflater.inflate(R.layout.item_rv_memo, parent, false) ;
        MemoAdapter.ViewHolder vh = new MemoAdapter.ViewHolder(view) ;

        return vh;
    }

    @Override
    public int getItemCount() {
        return items.size() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MemoAdapter.ViewHolder holder, final int position) {
        // 우선 오류가 있기는 한데 이게 진행 상에 크게 문제가 되지는 않아서 그냥 놔둠
        MemoItem item = items.get(position);
        // 메모 아이템 xml상에 메모 데이터가 적용되도록 세팅
        holder.setItem(item);

        // 메모 아이템 안에 있는 보기 버튼을 클릭하여 상세보기(ViewActivity)로 이동
        holder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ViewActivity.class);
                intent.putExtra("key",holder.date.getText().toString());
                mcontext.startActivity(intent);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, CancelPopup.class);
                intent.putExtra("key",holder.date.getText().toString());
                intent.putExtra("position",position);
                mcontext.startActivityForResult(intent,201);
                Log.d("Adapter","삭제 버튼 누름");
            }
        });


    }

    // 커스텀 뷰 홀더가 아님
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView title;
        Button view_btn;
        Button delete_btn;

        ViewHolder(View itemView){
            super(itemView);

            //뷰홀더에 필요한 아이템데이터 findview
            date = itemView.findViewById(R.id.date_contain);//아이템에 들어갈 텍스트
            title = itemView.findViewById(R.id.title_contain);//아이템에 들어갈 텍스트
            view_btn = itemView.findViewById(R.id.view_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
        //아이템뷰에 binding할 데이터
        public void setItem(MemoItem item) {
            date.setText(item.getDate());
            title.setText(item.getTitle());

        }
    }

}