package sabwa.jah;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainMenu extends ListActivity {
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);

String[] values = new String[] { "Send Market Data", "Submit Incidence", "Exit"};
ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
android.R.layout.simple_list_item_1, values);
setListAdapter(adapter);
}


@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
String item = (String) getListAdapter().getItem(position);
//Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
if (item =="Send Market Data")
{
Intent i = new Intent(MainMenu.this, Maize.class);
startActivity(i);
}
else if(item == "Submit Incidence")
{
Intent j = new Intent(MainMenu.this, Inc.class);
startActivity(j);
}

else
{
super.finish();
}
}
}
