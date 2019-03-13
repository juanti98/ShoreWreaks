package com.shorewreaks.juan.shorewreaks;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
           /* case 0:
                ChatFragment chatFragment= new ChatFragment();
                return chatFragment;*/
            case 0:
                GroupsFragment groupsFragment= new GroupsFragment();
                return groupsFragment;
           /* case 2:
                ContactsFragment contactsFragment= new ContactsFragment();
                return contactsFragment;*/

            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
           /* case 0:
                return "Chats";*/
            case 0:
                return "Groups";
           /* case 2:
                return "Contacts";*/

            default:
                return null;
        }
    }
}
