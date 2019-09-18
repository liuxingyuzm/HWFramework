package jcifs.smb;

public interface DosError {
    public static final int[][] DOS_ERROR_CODES = {new int[]{0, 0}, new int[]{65537, NtStatus.NT_STATUS_NOT_IMPLEMENTED}, new int[]{65538, NtStatus.NT_STATUS_NOT_IMPLEMENTED}, new int[]{131073, NtStatus.NT_STATUS_NO_SUCH_FILE}, new int[]{131074, NtStatus.NT_STATUS_WRONG_PASSWORD}, new int[]{196609, NtStatus.NT_STATUS_OBJECT_PATH_NOT_FOUND}, new int[]{196610, -1073741621}, new int[]{262146, NtStatus.NT_STATUS_NETWORK_ACCESS_DENIED}, new int[]{327681, NtStatus.NT_STATUS_ACCESS_DENIED}, new int[]{327682, NtStatus.NT_STATUS_INVALID_PARAMETER}, new int[]{393217, NtStatus.NT_STATUS_INVALID_HANDLE}, new int[]{393218, NtStatus.NT_STATUS_BAD_NETWORK_NAME}, new int[]{524289, -1073741670}, new int[]{1245187, -1073741662}, new int[]{1376259, -1073741805}, new int[]{2031617, NtStatus.NT_STATUS_UNSUCCESSFUL}, new int[]{2031619, NtStatus.NT_STATUS_UNSUCCESSFUL}, new int[]{2097153, NtStatus.NT_STATUS_SHARING_VIOLATION}, new int[]{2097155, NtStatus.NT_STATUS_SHARING_VIOLATION}, new int[]{2162691, -1073741740}, new int[]{2555907, -1073741697}, new int[]{3407873, NtStatus.NT_STATUS_DUPLICATE_NAME}, new int[]{4390913, NtStatus.NT_STATUS_BAD_NETWORK_NAME}, new int[]{4653057, NtStatus.NT_STATUS_REQUEST_NOT_ACCEPTED}, new int[]{5242881, NtStatus.NT_STATUS_OBJECT_NAME_COLLISION}, new int[]{5701633, NtStatus.NT_STATUS_INVALID_INFO_CLASS}, new int[]{5898242, -1073741618}, new int[]{5963778, NtStatus.NT_STATUS_INVALID_PARAMETER}, new int[]{7143425, NtStatus.NT_STATUS_PIPE_BROKEN}, new int[]{8060929, NtStatus.NT_STATUS_OBJECT_NAME_INVALID}, new int[]{9502721, -1073741567}, new int[]{11993089, NtStatus.NT_STATUS_OBJECT_NAME_COLLISION}, new int[]{15138817, NtStatus.NT_STATUS_INSTANCE_NOT_AVAILABLE}, new int[]{15204353, NtStatus.NT_STATUS_PIPE_CLOSING}, new int[]{15269889, NtStatus.NT_STATUS_PIPE_DISCONNECTED}, new int[]{15335425, NtStatus.NT_STATUS_MORE_PROCESSING_REQUIRED}, new int[]{146735106, -1073741421}, new int[]{146800642, NtStatus.NT_STATUS_INVALID_WORKSTATION}, new int[]{146866178, NtStatus.NT_STATUS_INVALID_LOGON_HOURS}, new int[]{146931714, NtStatus.NT_STATUS_PASSWORD_EXPIRED}};
    public static final String[] DOS_ERROR_MESSAGES = {"The operation completed successfully.", "Incorrect function.", "Incorrect function.", "The system cannot find the file specified.", "Bad password.", "The system cannot find the path specified.", "reserved", "The client does not have the necessary access rights to perform the requested function.", "Access is denied.", "The TID specified was invalid.", "The handle is invalid.", "The network name cannot be found.", "Not enough storage is available to process this command.", "The media is write protected.", "The device is not ready.", "A device attached to the system is not functioning.", "A device attached to the system is not functioning.", "The process cannot access the file because it is being used by another process.", "The process cannot access the file because it is being used by another process.", "The process cannot access the file because another process has locked a portion of the file.", "The disk is full.", "A duplicate name exists on the network.", "The network name cannot be found.", "ERRnomoreconn.", "The file exists.", "The parameter is incorrect.", "Too many Uids active on this session.", "The Uid is not known as a valid user identifier on this session.", "The pipe has been ended.", "The filename, directory name, or volume label syntax is incorrect.", "The directory is not empty.", "Cannot create a file when that file already exists.", "All pipe instances are busy.", "The pipe is being closed.", "No process is on the other end of the pipe.", "More data is available.", "This user account has expired.", "The user is not allowed to log on from this workstation.", "The user is not allowed to log on at this time.", "The password of this user has expired."};
}
