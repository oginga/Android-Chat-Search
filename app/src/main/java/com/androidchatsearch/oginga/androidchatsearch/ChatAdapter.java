package com.androidchatsearch.oginga.androidchatsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stewie on 2/5/18.
 */

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    ArrayList<String > singleChatList = new ArrayList<String>();
    Boolean convo=false;


    public ChatAdapter(Context context){
        String[] scriptArray = context.getResources().getStringArray(R.array.chat_script);
        Collections.addAll(singleChatList, scriptArray);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        final String msg=singleChatList.get(holder.getAdapterPosition());
        final ChatViewHolder viewHolder=holder;
        Log.e("ADAPTER-Pos--->",holder.getAdapterPosition()+" : "+msg);
        if (!convo){
            viewHolder.txtContentIncoming.setText(msg);
            viewHolder.txtContentIncoming.setVisibility(View.VISIBLE);
            hideTextView(viewHolder.textContentOutgoing);
            hideTextView(viewHolder.txtSpecialMessage);
            convo=true;
        }else{
            viewHolder.textContentOutgoing.setText(msg);
            viewHolder.textContentOutgoing.setVisibility(View.VISIBLE);
            hideTextView(viewHolder.txtContentIncoming);
            hideTextView(viewHolder.txtSpecialMessage);
            convo=false;

        }

    }

    @Override
    public int getItemCount() {
        return singleChatList.size();
    }

    public ArrayList<Integer> filter(String text) {
        ArrayList<Integer> foundIndexes=new ArrayList<Integer>();
        if(!text.isEmpty()){
            text = text.toLowerCase();
            for(String item: singleChatList){
                if(item.toLowerCase().contains(text) || item.toLowerCase().contains(text)){
//                    Log.e("SEARCH","found--->"+singleChatList.indexOf(item));
                    foundIndexes.add(singleChatList.indexOf(item));
                }
            }
        }
        return foundIndexes;
    }

    /*Chats View Holder*/
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textContentOutgoing, txtContentIncoming,txtSpecialMessage;

        public ChatViewHolder(View view) {
            super(view);
            textContentOutgoing = (TextView) view.findViewById(R.id.txt_content_outgoing);
            txtContentIncoming = (TextView) view.findViewById(R.id.txt_content_incoming);
            txtSpecialMessage = (TextView) view.findViewById(R.id.txt_special_message);

        }
    }

    private void hideTextView(TextView textView)
    {
        ViewGroup.LayoutParams params=textView.getLayoutParams();
        params.height=0;
        params.width=0;
        textView.setLayoutParams(params);
    }

}
