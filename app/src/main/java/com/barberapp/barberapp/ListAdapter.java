package com.barberapp.barberapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
/**
 * Created by Christian on 11-12-2017.
 */

public class ListAdapter extends ArrayAdapter<Subject> {

    public ArrayList<Subject> MainList;

    public ArrayList<Subject> SubjectListTemp;

    public ListAdapter.SubjectDataFilter subjectDataFilter ;

    public ListAdapter(Context context, int id, ArrayList<Subject> subjectArrayList) {

        super(context, id, subjectArrayList);

        this.SubjectListTemp = new ArrayList<Subject>();

        this.SubjectListTemp.addAll(subjectArrayList);

        this.MainList = new ArrayList<Subject>();

        this.MainList.addAll(subjectArrayList);
    }

    @Override
    public Filter getFilter() {

        if (subjectDataFilter == null){

            subjectDataFilter  = new ListAdapter.SubjectDataFilter();
        }
        return subjectDataFilter;
    }


    public class ViewHolder {

        TextView txtFecha;
        TextView txtHora;
        TextView txtSolicitud;
        TextView txtIdCliente;
        TextView txtIdBarbero;
        TextView txtAsientoAsignado;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.custom_layout2, null);

            holder = new ListAdapter.ViewHolder();

            holder.txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);

            holder.txtHora = (TextView) convertView.findViewById(R.id.txtHora);

            holder.txtSolicitud = (TextView) convertView.findViewById(R.id.txtSolicitud);

            holder.txtIdCliente = (TextView) convertView.findViewById(R.id.txtIdCliente);

            holder.txtIdBarbero = (TextView) convertView.findViewById(R.id.txtIdBarbero);

            holder.txtAsientoAsignado = (TextView) convertView.findViewById(R.id.txtAsientoAsignado);

            convertView.setTag(holder);

        }
        else{
            holder = (ListAdapter.ViewHolder) convertView.getTag();
        }

        Subject subject = SubjectListTemp.get(position);

        holder.txtFecha.setText(subject.getFecha());

        holder.txtHora.setText(subject.getHora());

        holder.txtSolicitud.setText(subject.getSolicitud());

        holder.txtIdCliente.setText(subject.getIdCliente());

        holder.txtIdBarbero.setText(subject.getIdBarbero());

        holder.txtAsientoAsignado.setText(subject.getAsiento());


        return convertView;

    }


    private class SubjectDataFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.toString().length() > 0)
            {
                ArrayList<Subject> arrayList1 = new ArrayList<Subject>();

                for(int i = 0, l = MainList.size(); i < l; i++)
                {
                    Subject subject = MainList.get(i);

                    if(subject.toString().toLowerCase().contains(charSequence))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            }
            else
            {
                synchronized(this)
                {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            SubjectListTemp = (ArrayList<Subject>)filterResults.values;

            notifyDataSetChanged();

            clear();

            for(int i = 0, l = SubjectListTemp.size(); i < l; i++)
                add(SubjectListTemp.get(i));

            notifyDataSetInvalidated();
        }
    }

}
