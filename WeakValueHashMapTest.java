package com.epublica.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class WeakValueHashMapTest
{

    // {{{ Helper class

    static class SpecialString {
        final String value;

        public SpecialString(String value)
        {
            super();
            this.value = value;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            SpecialString other = (SpecialString) obj;
            if (value == null) {
                if (other.value != null) return false;
            } else if (!value.equals(other.value)) return false;
            return true;
        }
    }

    // }}}

    WeakValueHashMap<String, Object> map;

    @BeforeEach
    public void setupMap() {
        map = new WeakValueHashMap<String, Object>();
    }

    @Nested
    public class StandardBehavior {

        @Test
        public void addsElement() throws Exception
        {
            assertEquals(0, map.size());

            String value = "bla";

            map.put("1", value);

            assertEquals(1, map.size());
        }

        @Test
        public void getsElements() throws Exception
        {
            String value = "bla";
            map.put("1", value);

            assertEquals("bla", map.get("1"));
        }

        @Test
        public void containsKey() throws Exception
        {
            Object value = new Object();
            map.put("1", value);

            assertTrue(map.containsKey("1"));
        }

        @Test
        public void containsValue() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);

            assertTrue(map.containsValue(new SpecialString("bla")));
        }


        @Test
        public void entrySetWorks() throws Exception
        {
            String value = "bla";
            map.put("1", value);
            for (Entry<String,Object> entry: map.entrySet()) {
                assertEquals("1",entry.getKey());
                assertEquals("bla",entry.getValue());
            }
        }


        @Test
        public void keySetWorks() throws Exception
        {
            String value = "bla";
            map.put("1", value);
            for (String entry: map.keySet()) {
                assertEquals("1",entry);
            }
        }

        @Test
        public void valuesWorks() throws Exception
        {
            String value = "bla";
            map.put("1", value);
            for (Object entry: map.values()) {
                assertEquals("bla",entry);
            }
        }

        @Test
        public void removesElements() throws Exception
        {
            String value = "bla";
            map.put("1", value);
            assertEquals(1, map.size());
            map.remove("1");
            assertEquals(0, map.size());
        }

    }

    @Nested
    public class WeakBehavior {

        protected void doGC()
        {
            for (int i = 0; i < 10; i++) {
                System.gc();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Test
        public void removesElementsWithoutReference() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);
            value = null;
            doGC();

            assertNull(map.get("1"));
        }

        @Test
        public void containsKey() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);
            value = null;
            doGC();

            assertFalse(map.containsKey("1"));
        }


        @Test
        public void containsValue() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);
            value = null;
            doGC();

            assertFalse(map.containsValue(new SpecialString("bla")));
        }

        @Test
        public void entrySetWorks() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);
            assertEquals(1,map.entrySet().size());
            value = null;
            doGC();

            assertEquals(0,map.entrySet().size());
        }

        @Test
        public void keySetWorks() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);
            assertEquals(1,map.keySet().size());
            value = null;
            doGC();

            assertEquals(0,map.keySet().size());
        }

        @Test
        public void valuesWorks() throws Exception
        {
            Object value = new SpecialString("bla");
            map.put("1", value);
            assertEquals(1,map.values().size());
            value = null;
            doGC();

            assertEquals(0,map.values().size());
        }


    }

}
