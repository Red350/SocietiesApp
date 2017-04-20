import pymysql

class Database:
    conn = pymysql.connect(host='localhost', port=3306, user='root', passwd='', db='societies')
    cur = conn.cursor()

    def check_session(self, member_id, session_id):
        sql = "SELECT * FROM session WHERE(member_id = " + member_id + " AND session_id = '" + session_id + "');"
        try:
            self.cur.execute(sql)
        except:
            return False
        if self.cur.rowcount >= 1:
            return True
        else:
            return False

    def check_member(self, member_id, society_id):
        sql = "SELECT * FROM member_society WHERE(member_id = " + member_id + " AND society_id = " + society_id + ")"
        try:
            self.cur.execute(sql)
        except:
            return False
        if self.cur.rowcount == 1:
            return True
        else:
            return False
    
    def check_committee(self, member_id, society_id):
        sql = "SELECT * FROM committee_society WHERE(member_id = " + member_id + " AND society_id = " + society_id + ")"
        try:
            self.cur.execute(sql)
        except:
            return False
        if self.cur.rowcount == 1:
            return True
        else:
            return False
    
    def check_chair(self, member_id, society_id):
        sql = "SELECT * FROM society WHERE(chair_id = " + member_id + " AND society_id = " + society_id + ")"
        try:
            self.cur.execute(sql)
        except:
            return false
        if self.cur.rowcount == 1:
            return True
        else:
            return False

    def log(self, query, exception):
        pass
        #todo: log exception
    
    def close(self):
        self.conn.commit()
        self.conn.close()

